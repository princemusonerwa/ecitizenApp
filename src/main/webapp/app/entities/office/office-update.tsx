import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getOffices } from 'app/entities/office/office.reducer';
import { IOffice } from 'app/shared/model/office.model';
import { OfficeType } from 'app/shared/model/enumerations/office-type.model';
import { getEntity, updateEntity, createEntity, reset } from './office.reducer';

export const OfficeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const offices = useAppSelector(state => state.office.entities);
  const officeEntity = useAppSelector(state => state.office.entity);
  const loading = useAppSelector(state => state.office.loading);
  const updating = useAppSelector(state => state.office.updating);
  const updateSuccess = useAppSelector(state => state.office.updateSuccess);
  const officeTypeValues = Object.keys(OfficeType);
  const handleClose = () => {
    props.history.push('/office');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getOffices({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...officeEntity,
      ...values,
      office: users.find(it => it.id.toString() === values.office.toString()),
      children: offices.find(it => it.id.toString() === values.children.toString()),
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
          officeType: 'MINALOC',
          ...officeEntity,
          office: officeEntity?.office?.id,
          children: officeEntity?.children?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecitizenApp.office.home.createOrEditLabel" data-cy="OfficeCreateUpdateHeading">
            Create or edit a Office
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="office-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Parent Id" id="office-parentId" name="parentId" data-cy="parentId" type="text" />
              <ValidatedField
                label="Name"
                id="office-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Office Type" id="office-officeType" name="officeType" data-cy="officeType" type="select">
                {officeTypeValues.map(officeType => (
                  <option value={officeType} key={officeType}>
                    {officeType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Created At"
                id="office-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="office-office" name="office" data-cy="office" label="Office" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="office-children" name="children" data-cy="children" label="Children" type="select">
                <option value="" key="0" />
                {offices
                  ? offices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/office" replace color="info">
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

export default OfficeUpdate;