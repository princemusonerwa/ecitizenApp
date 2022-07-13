import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cell from './cell';
import CellDetail from './cell-detail';
import CellUpdate from './cell-update';
import CellDeleteDialog from './cell-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CellUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CellUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CellDetail} />
      <ErrorBoundaryRoute path={match.url} component={Cell} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CellDeleteDialog} />
  </>
);

export default Routes;
