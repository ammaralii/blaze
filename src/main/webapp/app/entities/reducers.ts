import applicationUser from 'app/entities/application-user/application-user.reducer';
import company from 'app/entities/company/company.reducer';
import milestone from 'app/entities/milestone/milestone.reducer';
import permission from 'app/entities/permission/permission.reducer';
import project from 'app/entities/project/project.reducer';
import role from 'app/entities/role/role.reducer';
import section from 'app/entities/section/section.reducer';
import template from 'app/entities/template/template.reducer';
import templateField from 'app/entities/template-field/template-field.reducer';
import templateFieldType from 'app/entities/template-field-type/template-field-type.reducer';
import testCase from 'app/entities/test-case/test-case.reducer';
import testCaseAttachment from 'app/entities/test-case-attachment/test-case-attachment.reducer';
import testCaseField from 'app/entities/test-case-field/test-case-field.reducer';
import testCaseFieldAttachment from 'app/entities/test-case-field-attachment/test-case-field-attachment.reducer';
import testCasePriority from 'app/entities/test-case-priority/test-case-priority.reducer';
import testLevel from 'app/entities/test-level/test-level.reducer';
import testPlan from 'app/entities/test-plan/test-plan.reducer';
import testRun from 'app/entities/test-run/test-run.reducer';
import testRunDetailAttachment from 'app/entities/test-run-detail-attachment/test-run-detail-attachment.reducer';
import testRunDetails from 'app/entities/test-run-details/test-run-details.reducer';
import testRunStepDetailAttachment from 'app/entities/test-run-step-detail-attachment/test-run-step-detail-attachment.reducer';
import testRunStepDetails from 'app/entities/test-run-step-details/test-run-step-details.reducer';
import testStatus from 'app/entities/test-status/test-status.reducer';
import testSuite from 'app/entities/test-suite/test-suite.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  applicationUser,
  company,
  milestone,
  permission,
  project,
  role,
  section,
  template,
  templateField,
  templateFieldType,
  testCase,
  testCaseAttachment,
  testCaseField,
  testCaseFieldAttachment,
  testCasePriority,
  testLevel,
  testPlan,
  testRun,
  testRunDetailAttachment,
  testRunDetails,
  testRunStepDetailAttachment,
  testRunStepDetails,
  testStatus,
  testSuite,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
