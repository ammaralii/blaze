import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestSuite from './test-suite';
import TestSuiteDetail from './test-suite-detail';
import TestSuiteUpdate from './test-suite-update';
import TestSuiteDeleteDialog from './test-suite-delete-dialog';

const TestSuiteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestSuite />} />
    <Route path="new" element={<TestSuiteUpdate />} />
    <Route path=":id">
      <Route index element={<TestSuiteDetail />} />
      <Route path="edit" element={<TestSuiteUpdate />} />
      <Route path="delete" element={<TestSuiteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestSuiteRoutes;
