import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestRunStepDetails from './test-run-step-details';
import TestRunStepDetailsDetail from './test-run-step-details-detail';
import TestRunStepDetailsUpdate from './test-run-step-details-update';
import TestRunStepDetailsDeleteDialog from './test-run-step-details-delete-dialog';

const TestRunStepDetailsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestRunStepDetails />} />
    <Route path="new" element={<TestRunStepDetailsUpdate />} />
    <Route path=":id">
      <Route index element={<TestRunStepDetailsDetail />} />
      <Route path="edit" element={<TestRunStepDetailsUpdate />} />
      <Route path="delete" element={<TestRunStepDetailsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestRunStepDetailsRoutes;
