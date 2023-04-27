import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TemplateFieldType from './template-field-type';
import TemplateFieldTypeDetail from './template-field-type-detail';
import TemplateFieldTypeUpdate from './template-field-type-update';
import TemplateFieldTypeDeleteDialog from './template-field-type-delete-dialog';

const TemplateFieldTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TemplateFieldType />} />
    <Route path="new" element={<TemplateFieldTypeUpdate />} />
    <Route path=":id">
      <Route index element={<TemplateFieldTypeDetail />} />
      <Route path="edit" element={<TemplateFieldTypeUpdate />} />
      <Route path="delete" element={<TemplateFieldTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TemplateFieldTypeRoutes;
