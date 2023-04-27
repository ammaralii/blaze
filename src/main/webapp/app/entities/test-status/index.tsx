import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestStatus from './test-status';
import TestStatusDetail from './test-status-detail';
import TestStatusUpdate from './test-status-update';
import TestStatusDeleteDialog from './test-status-delete-dialog';

const TestStatusRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestStatus />} />
    <Route path="new" element={<TestStatusUpdate />} />
    <Route path=":id">
      <Route index element={<TestStatusDetail />} />
      <Route path="edit" element={<TestStatusUpdate />} />
      <Route path="delete" element={<TestStatusDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestStatusRoutes;
