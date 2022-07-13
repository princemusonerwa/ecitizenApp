import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Umurimo from './umurimo';
import UmurimoDetail from './umurimo-detail';
import UmurimoUpdate from './umurimo-update';
import UmurimoDeleteDialog from './umurimo-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UmurimoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UmurimoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UmurimoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Umurimo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UmurimoDeleteDialog} />
  </>
);

export default Routes;
