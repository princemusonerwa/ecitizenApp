import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IVillage } from 'app/shared/model/village.model';

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
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { getEntities as getVillages } from 'app/entities/village/village.reducer';

export const ComplainUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const categories = useAppSelector(state => state.category.entities);
  const umuturages = useAppSelector(state => state.umuturage.entities);
  const genderValues = Object.keys(Gender);

  const users = useAppSelector(state => state.userManagement.users);
  const organizations = useAppSelector(state => state.organization.entities);
  const complainEntity = useAppSelector(state => state.complain.entity);
  const loading = useAppSelector(state => state.complain.loading);
  const updating = useAppSelector(state => state.complain.updating);
  const updateSuccess = useAppSelector(state => state.complain.updateSuccess);
  const statusValues = Object.keys(Status);
  const priorityValues = Object.keys(Priority);
  const villages = useAppSelector(state => state.village.entities);

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
    dispatch(getVillages({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);
    values.dob = convertDateTimeToServer(values.dob);

    const entity = {
      ...complainEntity,
      ...values,
      organizations: mapIdList(values.organizations),
      category: categories.find(it => it.id.toString() === values.category.toString()),
      village: villages.find(it => it.id.toString() === values.village.toString()),
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
          createdAt: convertDateTimeFromServer(complainEntity.createdAt),
          updatedAt: convertDateTimeFromServer(complainEntity.updated),
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
                label="Indangamuntu"
                id="umuturage-indangamuntu"
                name="indangamuntu"
                data-cy="indangamuntu"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 16, message: 'This field is required to be at least 16 characters.' },
                  maxLength: { value: 16, message: 'This field cannot be longer than 16 characters.' },
                }}
              />
              <ValidatedField
                label="Amazina"
                id="umuturage-amazina"
                name="amazina"
                data-cy="amazina"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 3, message: 'This field is required to be at least 3 characters.' },
                  maxLength: { value: 255, message: 'This field cannot be longer than 255 characters.' },
                }}
              />
              <ValidatedField
                label="Dob"
                id="umuturage-dob"
                name="dob"
                data-cy="dob"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                value={displayDefaultDateTime()}
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Gender" id="umuturage-gender" name="gender" data-cy="gender" type="select">
                {genderValues.map(gender => (
                  <option value={gender} key={gender}>
                    {gender}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Ubudehe Category"
                id="umuturage-ubudeheCategory"
                name="ubudeheCategory"
                data-cy="ubudeheCategory"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 1, message: 'This field is required to be at least 1 characters.' },
                  maxLength: { value: 1, message: 'This field cannot be longer than 1 characters.' },
                }}
              />
              <ValidatedField
                label="Phone"
                id="umuturage-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  minLength: { value: 13, message: 'This field is required to be at least 13 characters.' },
                  maxLength: { value: 13, message: 'This field cannot be longer than 13 characters.' },
                }}
              />
              <ValidatedField
                label="Email"
                id="umuturage-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  pattern: {
                    value: /^[^@\s]+@[^@\s]+\.[^@\s]+$/,
                    message: "This field should follow pattern for '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+..",
                  },
                }}
              />
              <ValidatedField id="umuturage-village" name="village" data-cy="village" label="Village" type="select">
                <option value="" key="0" />
                {villages
                  ? villages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
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
              <ValidatedField
                label="Created At"
                id="complain-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                value={displayDefaultDateTime()}
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Updated At"
                id="complain-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                value={displayDefaultDateTime()}
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
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
