import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestRunDetailAttachment from './test-run-detail-attachment';
import TestRunDetailAttachmentDetail from './test-run-detail-attachment-detail';
import TestRunDetailAttachmentUpdate from './test-run-detail-attachment-update';
import TestRunDetailAttachmentDeleteDialog from './test-run-detail-attachment-delete-dialog';

const TestRunDetailAttachmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestRunDetailAttachment />} />
    <Route path="new" element={<TestRunDetailAttachmentUpdate />} />
    <Route path=":id">
      <Route index element={<TestRunDetailAttachmentDetail />} />
      <Route path="edit" element={<TestRunDetailAttachmentUpdate />} />
      <Route path="delete" element={<TestRunDetailAttachmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestRunDetailAttachmentRoutes;
