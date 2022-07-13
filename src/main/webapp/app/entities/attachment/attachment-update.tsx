import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IComplain } from 'app/shared/model/complain.model';
import { getEntities as getComplains } from 'app/entities/complain/complain.reducer';
import { IAttachment } from 'app/shared/model/attachment.model';
import { getEntity, updateEntity, createEntity, reset } from './attachment.reducer';

export const AttachmentUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const complains = useAppSelector(state => state.complain.entities);
  const attachmentEntity = useAppSelector(state => state.attachment.entity);
  const loading = useAppSelector(state => state.attachment.loading);
  const updating = useAppSelector(state => state.attachment.updating);
  const updateSuccess = useAppSelector(state => state.attachment.updateSuccess);
  const handleClose = () => {
    props.history.push('/attachment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getComplains({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...attachmentEntity,
      ...values,
      complain: complains.find(it => it.id.toString() === values.complain.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...attachmentEntity,
          complain: attachmentEntity?.complain?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecitizenApp.attachment.home.createOrEditLabel" data-cy="AttachmentCreateUpdateHeading">
            Create or edit a Attachment
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="attachment-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedBlobField
                label="Attachment Link"
                id="attachment-attachmentLink"
                name="attachmentLink"
                data-cy="attachmentLink"
                openActionLabel="Open"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Attachment Type"
                id="attachment-attachmentType"
                name="attachmentType"
                data-cy="attachmentType"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="attachment-complain" name="complain" data-cy="complain" label="Complain" type="select">
                <option value="" key="0" />
                {complains
                  ? complains.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/attachment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AttachmentUpdate;
