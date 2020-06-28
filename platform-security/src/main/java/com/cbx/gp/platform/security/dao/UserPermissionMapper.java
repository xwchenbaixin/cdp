package com.cbx.gp.platform.security.dao;

import java.util.List;

import com.cbx.gp.platform.security.model.Permission;
import com.cbx.gp.platform.security.model.Role;
import com.cbx.gp.platform.security.model.RolePermission;
import com.cbx.gp.platform.security.model.User;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserPermissionMapper {
	@Select("select * from cdp_user where username=#{username} and status=1")
	public User getUserByUsername(@Param("username") String workNo);

	@Select("SELECT r.* FROM cdp_user_role ur LEFT JOIN cdp_user u ON ur.user_id=u.id LEFT JOIN cdp_role r ON ur.role_id=r.id where ur.user_id=#{userId}")
	public List<Role> getRoleListByUserId(@Param("userId") int userId);

	@Select("SELECT * FROM cdp_role WHERE ID=#{roleId} and status=1")
	public Role getRoleById(@Param("roleId") int roleId);


	@Select("SELECT * FROM cdp_permission WHERE ID=#{permissionId}")
	public Permission getPermissionById(@Param("permissionId") int permissionId);
	
	@Select("SELECT *,RP.id AS rp_id FROM cdp_role_permission RP "
			+ "LEFT JOIN cdp_permission P ON RP.permission_ID=P.ID "
			+ "LEFT JOIN cdp_role R ON RP.role_ID = R.ID")
	@Results({
		@Result(property = "id",column = "rp_id"),
		@Result(property = "permission",column = "permission_id", one = @One(select = "com.cbx.gp.platform.security.dao.UserPermissionMapper.getPermissionById")),
		@Result(property = "role",column = "role_id" , one = @One(select = "com.cbx.gp.platform.security.dao.UserPermissionMapper.getRoleById"))
	})
	public List<RolePermission> getRolePermissions();
}
