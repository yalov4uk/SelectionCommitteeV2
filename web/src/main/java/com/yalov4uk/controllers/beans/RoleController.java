package com.yalov4uk.controllers.beans;

import com.yalov4uk.abstracts.BaseCrudController;
import com.yalov4uk.interfaces.abstracts.IBaseCrudService;
import com.yalov4uk.interfaces.beans.IRoleService;
import com.yalov4uk.dto.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/roles")
public class RoleController extends BaseCrudController<RoleDto> {

    private final IRoleService roleService;

    @Autowired
    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody RoleDto role) {
        return createCrud(role);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody RoleDto role) {
        return updateCrud(role);
    }

    protected IBaseCrudService<RoleDto> getService() {
        return roleService;
    }
}
