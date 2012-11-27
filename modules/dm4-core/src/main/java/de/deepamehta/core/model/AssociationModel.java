package de.deepamehta.core.model;

import de.deepamehta.core.Association;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



/**
 * Collection of the data that makes up an {@link Association}.
 *
 * @author <a href="mailto:jri@deepamehta.de">Jörg Richter</a>
 */
public class AssociationModel extends DeepaMehtaObjectModel {

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private RoleModel roleModel1;   // may be null in models used for an update operation
    private RoleModel roleModel2;   // may be null in models used for an update operation

    // ---------------------------------------------------------------------------------------------------- Constructors

    public AssociationModel(String typeUri, RoleModel roleModel1, RoleModel roleModel2) {
        this(typeUri, roleModel1, roleModel2, null);
    }

    public AssociationModel(String typeUri, RoleModel roleModel1, RoleModel roleModel2, CompositeValue composite) {
        this(-1, typeUri, roleModel1, roleModel2, null, composite);
    }

    // ### TODO: drop this
    public AssociationModel(long id, String typeUri) {
        super(id, typeUri);
    }

    public AssociationModel(long id, String typeUri, RoleModel roleModel1, RoleModel roleModel2) {
        this(id, typeUri, roleModel1, roleModel2, null, null);
    }

    public AssociationModel(long id, String typeUri, RoleModel roleModel1, RoleModel roleModel2, SimpleValue value,
                                                                                             CompositeValue composite) {
        super(id, null, typeUri, value, composite);     // uri=null
        this.roleModel1 = roleModel1;
        this.roleModel2 = roleModel2;
    }

    public AssociationModel(AssociationModel model) {
        this(model.getId(), model.getTypeUri(), model.getRoleModel1(), model.getRoleModel2());
    }

    public AssociationModel(JSONObject assocModel) {
        super(assocModel);
        try {
            if (assocModel.has("role_1")) {
                this.roleModel1 = parseRole(assocModel.getJSONObject("role_1"));
            }
            if (assocModel.has("role_2")) {
                this.roleModel2 = parseRole(assocModel.getJSONObject("role_2"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Parsing AssociationModel failed (JSONObject=" + assocModel + ")", e);
        }
    }

    // -------------------------------------------------------------------------------------------------- Public Methods

    public RoleModel getRoleModel1() {
        return roleModel1;
    }

    public RoleModel getRoleModel2() {
        return roleModel2;
    }

    // ---

    public void setRoleModel1(RoleModel roleModel) {
        this.roleModel1 = roleModel;
    }

    public void setRoleModel2(RoleModel roleModel) {
        this.roleModel2 = roleModel;
    }



    // === Serialization ===

    public JSONObject toJSON() {
        try {
            JSONObject o = super.toJSON();
            o.put("role_1", roleModel1.toJSON());
            o.put("role_2", roleModel2.toJSON());
            return o;
        } catch (Exception e) {
            throw new RuntimeException("Serialization failed (" + this + ")", e);
        }
    }



    // === Java API ===

    @Override
    public String toString() {
        return "association (" + super.toString() + ", roleModel1=" + roleModel1 + ", roleModel2=" + roleModel2 + ")";
    }



    // ------------------------------------------------------------------------------------------------- Private Methods

    private RoleModel parseRole(JSONObject roleModel) {
        if (roleModel.has("topic_id") || roleModel.has("topic_uri")) {
            return new TopicRoleModel(roleModel);
        } else if (roleModel.has("assoc_id")) {
            return new AssociationRoleModel(roleModel);
        } else {
            throw new RuntimeException("Parsing TopicRoleModel/AssociationRoleModel failed " +
                "(JSONObject=" + roleModel + ")");
        }
    }
}