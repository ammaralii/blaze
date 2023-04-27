import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './test-run-detail-attachment.reducer';

export const TestRunDetailAttachmentDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const testRunDetailAttachmentEntity = useAppSelector(state => state.testRunDetailAttachment.entity);
  const updateSuccess = useAppSelector(state => state.testRunDetailAttachment.updateSuccess);

  const handleClose = () => {
    navigate('/test-run-detail-attachment' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(testRunDetailAttachmentEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="testRunDetailAttachmentDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="blazeApp.testRunDetailAttachment.delete.question">
        Are you sure you want to delete Test Run Detail Attachment {testRunDetailAttachmentEntity.id}?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-testRunDetailAttachment" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default TestRunDetailAttachmentDeleteDialog;
