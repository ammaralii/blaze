import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestRunDetails from './test-run-details';
import TestRunDetailsDetail from './test-run-details-detail';
import TestRunDetailsUpdate from './test-run-details-update';
import TestRunDetailsDeleteDialog from './test-run-details-delete-dialog';

const TestRunDetailsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestRunDetails />} />
    <Route path="new" element={<TestRunDetailsUpdate />} />
    <Route path=":id">
      <Route index element={<TestRunDetailsDetail />} />
      <Route path="edit" element={<TestRunDetailsUpdate />} />
      <Route path="delete" element={<TestRunDetailsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestRunDetailsRoutes;
