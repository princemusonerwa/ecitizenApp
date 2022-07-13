import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Province from './province';
import ProvinceDetail from './province-detail';
import ProvinceUpdate from './province-update';
import ProvinceDeleteDialog from './province-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProvinceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProvinceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProvinceDetail} />
      <ErrorBoundaryRoute path={match.url} component={Province} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProvinceDeleteDialog} />
  </>
);

export default Routes;
