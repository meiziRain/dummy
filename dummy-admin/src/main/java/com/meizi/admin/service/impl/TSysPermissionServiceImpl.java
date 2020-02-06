package com.meizi.admin.service.impl;

import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meizi.admin.auth.DefaultJwtUserDetails;
import com.meizi.admin.constants.AdminConstants;
import com.meizi.admin.entity.TSysPermission;
import com.meizi.admin.entity.TSysRole;
import com.meizi.admin.entity.TSysRolePermission;
import com.meizi.admin.entity.TSysUser;
import com.meizi.admin.mapper.TSysPermissionMapper;
import com.meizi.admin.mapper.TSysRolePermissionMapper;
import com.meizi.admin.model.Result;
import com.meizi.admin.service.ITSysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限信息表 服务实现类
 * </p>
 *
 * @author meizi
 * @since 2020-01-07
 */
@Service
public class TSysPermissionServiceImpl extends ServiceImpl<TSysPermissionMapper, TSysPermission> implements ITSysPermissionService {


    @Resource
    private TSysRolePermissionMapper tSysRolePermissionMapper;



    @Override
    public Object getPermissionTree(List<TSysPermission> permissions) {
        // 定义权限树存储对象
        List<Map<String, Object>> list = Lists.newLinkedList();
        permissions.forEach(permissionDomain -> {
                    if (Objects.nonNull(permissionDomain)) {
                        List<TSysPermission> permissionList = findByPid(permissionDomain.getId());
                        Map<String, Object> map = new HashMap<>(16);
                        map.put(AdminConstants.ADMIN_ID_KEY, permissionDomain.getId());
                        map.put(AdminConstants.ADMIN_LABEL_KEY, permissionDomain.getAlias());
                        if (CollectionUtils.isNotEmpty(permissionList)) {
                            map.put(AdminConstants.ADMIN_CHILDREN_KEY, getPermissionTree(permissionList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }

    @Override
    public Object getMenuTreeByList(List<TSysPermission> tSysPermissionList) {
        tSysPermissionList.sort(Comparator.comparing(TSysPermission::getPermissionName));
        List<TSysPermission> tSysPermissions = buildListToTree(tSysPermissionList);
        return buildTreeByList(tSysPermissions);
    }

    private Object buildTreeByList(List<TSysPermission> tSysPermissionList){
        List<Map<String, Object>> list = Lists.newLinkedList();
        tSysPermissionList.forEach(menuDomain -> {
                    if (Objects.nonNull(menuDomain)) {
                        Map<String, Object> retMap = Maps.newHashMap();
                        retMap.put(AdminConstants.ADMIN_ID_KEY, menuDomain.getId());
                        retMap.put(AdminConstants.ADMIN_LABEL_KEY, menuDomain.getAlias());
                        if (CollectionUtils.isNotEmpty(menuDomain.getChildren())) {
                            retMap.put(AdminConstants.ADMIN_CHILDREN_KEY, buildTreeByList(menuDomain.getChildren()));
                        }
                        list.add(retMap);
                    }
                }
        );
        return list;
    }

    private List<TSysPermission> buildListToTree(List<TSysPermission> dirs) {
        List<TSysPermission> roots = findRoots(dirs);
        List<TSysPermission> notRoots = (List<TSysPermission>) CollectionUtils
                .subtract(dirs, roots);
        for (TSysPermission root : roots) {
            root.setChildren(findChildren(root, notRoots));
        }
        return roots;
    }

    private List<TSysPermission> findChildren(TSysPermission root, List<TSysPermission> allNodes) {
        List<TSysPermission> children = new ArrayList<>();
        for (TSysPermission comparedOne : allNodes) {
            if (comparedOne.getPid().equals(root.getId())) {
                children.add(comparedOne);
            }
        }
        List<TSysPermission> notChildren = (List<TSysPermission>) CollectionUtils.subtract(allNodes, children);
        for (TSysPermission child : children) {
            List<TSysPermission> tmpChildren = findChildren(child, notChildren);
            child.setChildren(tmpChildren);
        }
        return children;
    }

    private List<TSysPermission> findRoots(List<TSysPermission> allNodes) {
        List<TSysPermission> results = new ArrayList<>();
        for (TSysPermission node : allNodes) {
            boolean isRoot = true;
            for (TSysPermission comparedOne : allNodes) {
                if (node.getPid().equals(comparedOne.getId())) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                results.add(node);
            }
        }
        return results;
    }


    /**
     * 注意:这里需要根据role去查询一遍permissions信息。
     * 现在role.getPermissions是没有数据的
     *
     * @param user
     * @return
     */
    @Override
    public Collection<GrantedAuthority> mapToGrantedAuthorities(TSysUser user) {
        List<TSysRole> roles = user.getRoles();
        return roles.stream().flatMap(role -> findByRoleId(role.getId()).stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
                .collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询所拥有的权限信息
     *
     * @param roleId
     * @return
     */
    @Override
    public List<TSysPermission> findByRoleId(long roleId) {
        return baseMapper.findByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result create(DefaultJwtUserDetails userDetails, TSysPermission domain) {
        if (domain.getId() != null) {
            throw new RuntimeException("不能传入id");
        }
        QueryWrapper<TSysPermission> tSysPermissionQueryWrapper = new QueryWrapper<>();
        tSysPermissionQueryWrapper.eq("permission_name", domain.getPermissionName());
        List<TSysPermission> tSysPermissionList = this.list(tSysPermissionQueryWrapper);
        if (CollectionUtils.isNotEmpty(tSysPermissionList)) {
            throw new RuntimeException("已存在相同的权限名称");
        }
        // 增加创建人修改人
        domain.setCreateBy(userDetails.getId());
        domain.setUpdateBy(userDetails.getId());
        if (domain.getPid() == null) {
            return Result.failed("请选择上级类目");
        }
        return Result.succeed(this.create(domain));
    }

    @Override
    public boolean create(TSysPermission domain) {
        boolean isSuccess = retBool(baseMapper.insert(domain));
        if (isMoreThanFive(domain.getId())) {
            throw new RuntimeException("权限树不能超过五层");
        }
        return isSuccess;
    }

    private Boolean isMoreThanFive(Long id) {
        Long selectId = id;
        for (int i = 0; i < AdminConstants.TREE_MAX_DEEP + 1; i++) {
            TSysPermission tSysPermission = this.findById(selectId);
            if (null != tSysPermission) {
                selectId = tSysPermission.getPid();
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public TSysPermission findById(long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<TSysPermission> selectPermissionsByCondition(String name) {
        QueryWrapper<TSysPermission> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), "PERMISSION_NAME", name);
        wrapper.orderByAsc("CONVERT(permission_name USING gbk)");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List buildTree(List<TSysPermission> permissionDomains) {
        int size = permissionDomains.size();
        //构建树结构
        for (int i = 0; i < size; i++) {
            TSysPermission tSysPermission = permissionDomains.get(i);
            for (int j = 0; j < size; j++) {
                TSysPermission matchedPermission = permissionDomains.get(j);
                if (matchedPermission.getPid().equals(tSysPermission.getId())) {
                    matchedPermission.setIsChild(true);
                    if (tSysPermission.getChildren() == null) {
                        tSysPermission.setChildren(new ArrayList<>());
                    }
                    tSysPermission.getChildren().add(matchedPermission);
                }
            }
        }
        //去掉列表中为其它节点子节点的数据
        Iterator<TSysPermission> it = permissionDomains.iterator();
        while (it.hasNext()) {
            TSysPermission tSysPermission = it.next();
            if (tSysPermission.getIsChild()) {
                it.remove();
            }
        }
        // 构建权限树返回
        return permissionDomains;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(DefaultJwtUserDetails userDetails, TSysPermission domain){
        domain.setUpdateBy(userDetails.getId());
        QueryWrapper<TSysPermission> tSysPermissionQueryWrapper = new QueryWrapper<>();
        tSysPermissionQueryWrapper.eq("permission_name", domain.getPermissionName());
        List<TSysPermission> tSysPermissionList = this.list(tSysPermissionQueryWrapper);
        tSysPermissionList.forEach(tSysPermission -> {
            if( ! tSysPermission.getId().equals(domain.getId())){
                throw new RuntimeException("已存在相同的权限名称");
            }
        });
        if(domain.getPid().equals(domain.getId())){
            throw new RuntimeException("上级类目不能为自己");
        }
        if(this.update(domain)) {
            return Result.succeed("修改权限成功");
        }
        return Result.failed("修改权限失败");
    }

    @Override
    public boolean update(TSysPermission domain) {
        if(isMoreThanFive(domain.getId())){
            throw new RuntimeException("权限树不能超过五层");
        }
        return retBool(baseMapper.updateById(domain));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        deleteChildren(id);
        // 删除本级菜单与角色关联关系
        QueryWrapper<TSysRolePermission> wrapper = new QueryWrapper<>();
        wrapper.eq("PERMISSION_ID", id);
        tSysRolePermissionMapper.delete(wrapper);
        return retBool(baseMapper.deleteById(id));
    }

    private void deleteChildren(Long id){
        List<TSysPermission> permissionList = findByPid(id);
        // 对子菜单删除进行处理
        for (TSysPermission permissionDomain : permissionList) {
            QueryWrapper<TSysRolePermission> wrapper = new QueryWrapper<>();
            wrapper.eq("PERMISSION_ID", permissionDomain.getId());
            tSysRolePermissionMapper.delete(wrapper);
            baseMapper.deleteById(permissionDomain.getId());

            deleteChildren(permissionDomain.getId());
        }
    }

    @Override
    public List<TSysPermission> findByPid(long pid) {
        QueryWrapper<TSysPermission> wrapper = new QueryWrapper<>();
        wrapper.eq(AdminConstants.ADMIN_PID_KEY, pid);
        wrapper.orderByAsc("permission_name");
        return baseMapper.selectList(wrapper);
    }

}
