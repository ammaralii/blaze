import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestCaseFieldAttachment from './test-case-field-attachment';
import TestCaseFieldAttachmentDetail from './test-case-field-attachment-detail';
import TestCaseFieldAttachmentUpdate from './test-case-field-attachment-update';
import TestCaseFieldAttachmentDeleteDialog from './test-case-field-attachment-delete-dialog';

const TestCaseFieldAttachmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestCaseFieldAttachment />} />
    <Route path="new" element={<TestCaseFieldAttachmentUpdate />} />
    <Route path=":id">
      <Route index element={<TestCaseFieldAttachmentDetail />} />
      <Route path="edit" element={<TestCaseFieldAttachmentUpdate />} />
      <Route path="delete" element={<TestCaseFieldAttachmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestCaseFieldAttachmentRoutes;
