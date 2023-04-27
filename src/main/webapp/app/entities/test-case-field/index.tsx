import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestCaseField from './test-case-field';
import TestCaseFieldDetail from './test-case-field-detail';
import TestCaseFieldUpdate from './test-case-field-update';
import TestCaseFieldDeleteDialog from './test-case-field-delete-dialog';

const TestCaseFieldRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestCaseField />} />
    <Route path="new" element={<TestCaseFieldUpdate />} />
    <Route path=":id">
      <Route index element={<TestCaseFieldDetail />} />
      <Route path="edit" element={<TestCaseFieldUpdate />} />
      <Route path="delete" element={<TestCaseFieldDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestCaseFieldRoutes;
