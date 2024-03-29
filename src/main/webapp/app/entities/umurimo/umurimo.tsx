import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUmurimo } from 'app/shared/model/umurimo.model';
import { getEntities } from './umurimo.reducer';

export const Umurimo = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const umurimoList = useAppSelector(state => state.umurimo.entities);
  const loading = useAppSelector(state => state.umurimo.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="umurimo-heading" data-cy="UmurimoHeading">
        Umurimos
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/umurimo/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Umurimo
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {umurimoList && umurimoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Umurimo</th>
                <th>Level</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {umurimoList.map((umurimo, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/umurimo/${umurimo.id}`} color="link" size="sm">
                      {umurimo.id}
                    </Button>
                  </td>
                  <td>{umurimo.umurimo}</td>
                  <td>{umurimo.officeType}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/umurimo/${umurimo.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/umurimo/${umurimo.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/umurimo/${umurimo.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Umurimos found</div>
        )}
      </div>
    </div>
  );
};

export default Umurimo;
