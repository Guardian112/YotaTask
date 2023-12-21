package steps;
import steps.apiSteps.AdminSteps;
import steps.apiSteps.UserSteps;
public interface Steps {
    AdminSteps ADMIN_STEPS = new AdminSteps();
    UserSteps USER_STEPS = new UserSteps();
}
