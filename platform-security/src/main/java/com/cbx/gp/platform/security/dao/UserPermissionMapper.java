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
	@Select("SELECT *,R.name as role_name,U.id as id FROM user U LEFT JOIN role R ON U.role_id=R.id WHERE work_no=#{workNo} AND U.state=1")
	public User getUserByWorkNo(@Param("workNo") String workNo);

	@Select("SELECT * FROM role WHERE ID=#{roleId} and state=1")
	public Role getRoleListById(@Param("roleId") int roleId);
	
	
	@Select("SELECT * FROM permission WHERE ID=#{permissionId}")
	public Permission getPermissions(@Param("permissionId") int permissionId);
	
	@Select("SELECT *,RP.id AS rp_id FROM role_permission RP "
			+ "LEFT JOIN permission P ON RP.permission_ID=P.ID "
			+ "LEFT JOIN role R ON RP.role_ID = R.ID")
	@Results({
		@Result(property = "id",column = "rp_id"),
		@Result(property = "permission",column = "permission_id", one = @One(select = "com.nchu.cbx.security.dao.UserPermissionMapper.getPermissions")),
		@Result(property = "role",column = "role_id" , one = @One(select = "com.nchu.cbx.security.dao.UserPermissionMapper.getRoleListById"))
	})
	public List<RolePermission> getRolePermissions();
}
