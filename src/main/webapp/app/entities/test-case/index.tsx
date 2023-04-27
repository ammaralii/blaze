import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestCase from './test-case';
import TestCaseDetail from './test-case-detail';
import TestCaseUpdate from './test-case-update';
import TestCaseDeleteDialog from './test-case-delete-dialog';

const TestCaseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestCase />} />
    <Route path="new" element={<TestCaseUpdate />} />
    <Route path=":id">
      <Route index element={<TestCaseDetail />} />
      <Route path="edit" element={<TestCaseUpdate />} />
      <Route path="delete" element={<TestCaseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestCaseRoutes;
