import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestPlan from './test-plan';
import TestPlanDetail from './test-plan-detail';
import TestPlanUpdate from './test-plan-update';
import TestPlanDeleteDialog from './test-plan-delete-dialog';

const TestPlanRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestPlan />} />
    <Route path="new" element={<TestPlanUpdate />} />
    <Route path=":id">
      <Route index element={<TestPlanDetail />} />
      <Route path="edit" element={<TestPlanUpdate />} />
      <Route path="delete" element={<TestPlanDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestPlanRoutes;
