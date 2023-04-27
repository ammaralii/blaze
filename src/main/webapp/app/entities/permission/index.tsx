import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Permission from './permission';
import PermissionDetail from './permission-detail';
import PermissionUpdate from './permission-update';
import PermissionDeleteDialog from './permission-delete-dialog';

const PermissionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Permission />} />
    <Route path="new" element={<PermissionUpdate />} />
    <Route path=":id">
      <Route index element={<PermissionDetail />} />
      <Route path="edit" element={<PermissionUpdate />} />
      <Route path="delete" element={<PermissionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PermissionRoutes;
