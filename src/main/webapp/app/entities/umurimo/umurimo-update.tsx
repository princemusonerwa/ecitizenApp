import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUmurimo } from 'app/shared/model/umurimo.model';
import { OfficeType } from 'app/shared/model/enumerations/office-type.model';
import { getEntity, updateEntity, createEntity, reset } from './umurimo.reducer';

export const UmurimoUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const umurimoEntity = useAppSelector(state => state.umurimo.entity);
  const loading = useAppSelector(state => state.umurimo.loading);
  const updating = useAppSelector(state => state.umurimo.updating);
  const updateSuccess = useAppSelector(state => state.umurimo.updateSuccess);
  const officeTypeValues = Object.keys(OfficeType);
  const handleClose = () => {
    props.history.push('/umurimo');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...umurimoEntity,
      ...values,
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
          ...umurimoEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecitizenApp.umurimo.home.createOrEditLabel" data-cy="UmurimoCreateUpdateHeading">
            Create or edit a Umurimo
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="umurimo-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Umurimo"
                id="umurimo-umurimo"
                name="umurimo"
                data-cy="umurimo"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 3, message: 'This field is required to be at least 3 characters.' },
                  maxLength: { value: 100, message: 'This field cannot be longer than 100 characters.' },
                }}
              />
              <ValidatedField label="Office Type" id="umurimo-officeType" name="officeType" data-cy="officeType" type="select">
                {officeTypeValues.map(officeType => (
                  <option value={officeType} key={officeType}>
                    {officeType}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/umurimo" replace color="info">
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

export default UmurimoUpdate;
