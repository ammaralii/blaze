import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestCasePriority from './test-case-priority';
import TestCasePriorityDetail from './test-case-priority-detail';
import TestCasePriorityUpdate from './test-case-priority-update';
import TestCasePriorityDeleteDialog from './test-case-priority-delete-dialog';

const TestCasePriorityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestCasePriority />} />
    <Route path="new" element={<TestCasePriorityUpdate />} />
    <Route path=":id">
      <Route index element={<TestCasePriorityDetail />} />
      <Route path="edit" element={<TestCasePriorityUpdate />} />
      <Route path="delete" element={<TestCasePriorityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestCasePriorityRoutes;
