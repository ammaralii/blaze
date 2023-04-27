import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TemplateField from './template-field';
import TemplateFieldDetail from './template-field-detail';
import TemplateFieldUpdate from './template-field-update';
import TemplateFieldDeleteDialog from './template-field-delete-dialog';

const TemplateFieldRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TemplateField />} />
    <Route path="new" element={<TemplateFieldUpdate />} />
    <Route path=":id">
      <Route index element={<TemplateFieldDetail />} />
      <Route path="edit" element={<TemplateFieldUpdate />} />
      <Route path="delete" element={<TemplateFieldDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TemplateFieldRoutes;
