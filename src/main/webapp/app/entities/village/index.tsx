import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Village from './village';
import VillageDetail from './village-detail';
import VillageUpdate from './village-update';
import VillageDeleteDialog from './village-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VillageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VillageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VillageDetail} />
      <ErrorBoundaryRoute path={match.url} component={Village} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VillageDeleteDialog} />
  </>
);

export default Routes;
