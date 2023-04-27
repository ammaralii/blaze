import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestCaseAttachment from './test-case-attachment';
import TestCaseAttachmentDetail from './test-case-attachment-detail';
import TestCaseAttachmentUpdate from './test-case-attachment-update';
import TestCaseAttachmentDeleteDialog from './test-case-attachment-delete-dialog';

const TestCaseAttachmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestCaseAttachment />} />
    <Route path="new" element={<TestCaseAttachmentUpdate />} />
    <Route path=":id">
      <Route index element={<TestCaseAttachmentDetail />} />
      <Route path="edit" element={<TestCaseAttachmentUpdate />} />
      <Route path="delete" element={<TestCaseAttachmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestCaseAttachmentRoutes;
