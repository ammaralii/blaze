import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestRunStepDetailAttachment from './test-run-step-detail-attachment';
import TestRunStepDetailAttachmentDetail from './test-run-step-detail-attachment-detail';
import TestRunStepDetailAttachmentUpdate from './test-run-step-detail-attachment-update';
import TestRunStepDetailAttachmentDeleteDialog from './test-run-step-detail-attachment-delete-dialog';

const TestRunStepDetailAttachmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestRunStepDetailAttachment />} />
    <Route path="new" element={<TestRunStepDetailAttachmentUpdate />} />
    <Route path=":id">
      <Route index element={<TestRunStepDetailAttachmentDetail />} />
      <Route path="edit" element={<TestRunStepDetailAttachmentUpdate />} />
      <Route path="delete" element={<TestRunStepDetailAttachmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestRunStepDetailAttachmentRoutes;
