import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ApplicationUser from './application-user';
import Company from './company';
import Milestone from './milestone';
import Permission from './permission';
import Project from './project';
import Role from './role';
import Section from './section';
import Template from './template';
import TemplateField from './template-field';
import TemplateFieldType from './template-field-type';
import TestCase from './test-case';
import TestCaseAttachment from './test-case-attachment';
import TestCaseField from './test-case-field';
import TestCaseFieldAttachment from './test-case-field-attachment';
import TestCasePriority from './test-case-priority';
import TestLevel from './test-level';
import TestPlan from './test-plan';
import TestRun from './test-run';
import TestRunDetailAttachment from './test-run-detail-attachment';
import TestRunDetails from './test-run-details';
import TestRunStepDetailAttachment from './test-run-step-detail-attachment';
import TestRunStepDetails from './test-run-step-details';
import TestStatus from './test-status';
import TestSuite from './test-suite';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="application-user/*" element={<ApplicationUser />} />
        <Route path="company/*" element={<Company />} />
        <Route path="milestone/*" element={<Milestone />} />
        <Route path="permission/*" element={<Permission />} />
        <Route path="project/*" element={<Project />} />
        <Route path="role/*" element={<Role />} />
        <Route path="section/*" element={<Section />} />
        <Route path="template/*" element={<Template />} />
        <Route path="template-field/*" element={<TemplateField />} />
        <Route path="template-field-type/*" element={<TemplateFieldType />} />
        <Route path="test-case/*" element={<TestCase />} />
        <Route path="test-case-attachment/*" element={<TestCaseAttachment />} />
        <Route path="test-case-field/*" element={<TestCaseField />} />
        <Route path="test-case-field-attachment/*" element={<TestCaseFieldAttachment />} />
        <Route path="test-case-priority/*" element={<TestCasePriority />} />
        <Route path="test-level/*" element={<TestLevel />} />
        <Route path="test-plan/*" element={<TestPlan />} />
        <Route path="test-run/*" element={<TestRun />} />
        <Route path="test-run-detail-attachment/*" element={<TestRunDetailAttachment />} />
        <Route path="test-run-details/*" element={<TestRunDetails />} />
        <Route path="test-run-step-detail-attachment/*" element={<TestRunStepDetailAttachment />} />
        <Route path="test-run-step-details/*" element={<TestRunStepDetails />} />
        <Route path="test-status/*" element={<TestStatus />} />
        <Route path="test-suite/*" element={<TestSuite />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
