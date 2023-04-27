import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestLevel from './test-level';
import TestLevelDetail from './test-level-detail';
import TestLevelUpdate from './test-level-update';
import TestLevelDeleteDialog from './test-level-delete-dialog';

const TestLevelRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestLevel />} />
    <Route path="new" element={<TestLevelUpdate />} />
    <Route path=":id">
      <Route index element={<TestLevelDetail />} />
      <Route path="edit" element={<TestLevelUpdate />} />
      <Route path="delete" element={<TestLevelDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestLevelRoutes;
