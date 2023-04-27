import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './test-case-field-attachment.reducer';

export const TestCaseFieldAttachmentDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const testCaseFieldAttachmentEntity = useAppSelector(state => state.testCaseFieldAttachment.entity);
  const updateSuccess = useAppSelector(state => state.testCaseFieldAttachment.updateSuccess);

  const handleClose = () => {
    navigate('/test-case-field-attachment' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(testCaseFieldAttachmentEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="testCaseFieldAttachmentDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="blazeApp.testCaseFieldAttachment.delete.question">
        Are you sure you want to delete Test Case Field Attachment {testCaseFieldAttachmentEntity.id}?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-testCaseFieldAttachment" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default TestCaseFieldAttachmentDeleteDialog;
