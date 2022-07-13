import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { IUmuturage } from 'app/shared/model/umuturage.model';
import { getEntities as getUmuturages } from 'app/entities/umuturage/umuturage.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IOrganization } from 'app/shared/model/organization.model';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { IComplain } from 'app/shared/model/complain.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { Priority } from 'app/shared/model/enumerations/priority.model';
import { getEntity, updateEntity, createEntity, reset } from './complain.reducer';

export const ComplainUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const categories = useAppSelector(state => state.category.entities);
  const umuturages = useAppSelector(state => state.umuturage.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const organizations = useAppSelector(state => state.organization.entities);
  const complainEntity = useAppSelector(state => state.complain.entity);
  const loading = useAppSelector(state => state.complain.loading);
  const updating = useAppSelector(state => state.complain.updating);
  const updateSuccess = useAppSelector(state => state.complain.updateSuccess);
  const statusValues = Object.keys(Status);
  const priorityValues = Object.keys(Priority);
  const handleClose = () => {
    props.history.push('/complain' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCategories({}));
    dispatch(getUmuturages({}));
    dispatch(getOrganizations({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...complainEntity,
      ...values,
      organizations: mapIdList(values.organizations),
      category: categories.find(it => it.id.toString() === values.category.toString()),
      umuturage: umuturages.find(it => it.id.toString() === values.umuturage.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING_REVIEW',
          priority: 'HIGH',
          ...complainEntity,
          date: convertDateTimeFromServer(complainEntity.date),
          category: complainEntity?.category?.id,
          umuturage: complainEntity?.umuturage?.id,
          organizations: complainEntity?.organizations?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecitizenApp.complain.home.createOrEditLabel" data-cy="ComplainCreateUpdateHeading">
            Create or edit a Complain
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="complain-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Ikibazo"
                id="complain-ikibazo"
                name="ikibazo"
                data-cy="ikibazo"
                type="textarea"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Icyakozwe"
                id="complain-icyakozwe"
                name="icyakozwe"
                data-cy="icyakozwe"
                type="textarea"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Icyakorwa" id="complain-icyakorwa" name="icyakorwa" data-cy="icyakorwa" type="textarea" />
              <ValidatedField label="Umwanzuro" id="complain-umwanzuro" name="umwanzuro" data-cy="umwanzuro" type="textarea" />
              <ValidatedField
                label="Date"
                id="complain-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Status" id="complain-status" name="status" data-cy="status" type="select">
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {status}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Priority" id="complain-priority" name="priority" data-cy="priority" type="select">
                {priorityValues.map(priority => (
                  <option value={priority} key={priority}>
                    {priority}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="complain-category" name="category" data-cy="category" label="Category" type="select">
                <option value="" key="0" />
                {categories
                  ? categories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="complain-umuturage" name="umuturage" data-cy="umuturage" label="Umuturage" type="select">
                <option value="" key="0" />
                {umuturages
                  ? umuturages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label="Organization"
                id="complain-organization"
                data-cy="organization"
                type="select"
                multiple
                name="organizations"
              >
                <option value="" key="0" />
                {organizations
                  ? organizations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/complain" replace color="info">
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

export default ComplainUpdate;
