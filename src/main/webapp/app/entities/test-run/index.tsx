import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestRun from './test-run';
import TestRunDetail from './test-run-detail';
import TestRunUpdate from './test-run-update';
import TestRunDeleteDialog from './test-run-delete-dialog';

const TestRunRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestRun />} />
    <Route path="new" element={<TestRunUpdate />} />
    <Route path=":id">
      <Route index element={<TestRunDetail />} />
      <Route path="edit" element={<TestRunUpdate />} />
      <Route path="delete" element={<TestRunDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestRunRoutes;
