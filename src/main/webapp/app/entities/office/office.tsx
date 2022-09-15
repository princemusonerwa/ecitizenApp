import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOffice } from 'app/shared/model/office.model';
import { getEntities } from './office.reducer';

export const Office = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const officeList = useAppSelector(state => state.office.entities);
  const loading = useAppSelector(state => state.office.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="office-heading" data-cy="OfficeHeading">
        Offices
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/office/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Office
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {officeList && officeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Office Type</th>
                <th>Name</th>
                <th>Created At</th>
                {/* <th>User</th> */}
                <th>Parent</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {officeList.map((office, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/office/${office.id}`} color="link" size="sm">
                      {office.id}
                    </Button>
                  </td>
                  <td>{office.officeType}</td>
                  <td>{office.name}</td>
                  <td>{office.createdAt ? <TextFormat type="date" value={office.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  {/* <td>{office.user ? office.user.login : ''}</td> */}
                  <td>{office.parent ? <Link to={`/office/${office.parent.id}`}>{office.parent.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/office/${office.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/office/${office.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/office/${office.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Offices found</div>
        )}
      </div>
    </div>
  );
};

export default Office;
