package uptc.edu.co.ms_auth.auth.dto;

import java.util.List;

public class RoleAssignmentRequest {

    private List<String> add;
    private List<String> remove;

    public RoleAssignmentRequest() {
    }

    public List<String> getAdd() {
        return add;
    }

    public void setAdd(List<String> add) {
        this.add = add;
    }

    public List<String> getRemove() {
        return remove;
    }

    public void setRemove(List<String> remove) {
        this.remove = remove;
    }
}
