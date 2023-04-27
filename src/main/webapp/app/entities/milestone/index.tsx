import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Milestone from './milestone';
import MilestoneDetail from './milestone-detail';
import MilestoneUpdate from './milestone-update';
import MilestoneDeleteDialog from './milestone-delete-dialog';

const MilestoneRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Milestone />} />
    <Route path="new" element={<MilestoneUpdate />} />
    <Route path=":id">
      <Route index element={<MilestoneDetail />} />
      <Route path="edit" element={<MilestoneUpdate />} />
      <Route path="delete" element={<MilestoneDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MilestoneRoutes;
