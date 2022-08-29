import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getSortState, JhiPagination, JhiItemCount, containerSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IComplain } from 'app/shared/model/complain.model';
import { getEntities } from './complain.reducer';

export const Complain = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();
  const [keyword, setKeyword] = useState('');

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const complainList = useAppSelector(state => state.complain.entities);
  const loading = useAppSelector(state => state.complain.loading);
  const totalItems = useAppSelector(state => state.complain.totalItems);

  const getAllEntities = () => {

    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        keyword,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    console.log(`This is the props: ${props.history}`);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        keyword: '',
      })
    );
  };

  const handleSearch = e => {
    e.preventDefault();
    getAllEntities();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="complain-heading" data-cy="ComplainHeading">
        Complains
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/complain/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Complain
          </Link>
        </div>
      </h2>
      <div className="row justify-content-center">
        <div className="col-4">
          <form className="d-flex search-form" role="search" onSubmit={handleSearch}>
            <input
              className="form-control me-2"
              type="search"
              placeholder="Search"
              aria-label="Search"
              value={keyword}
              onChange={e => setKeyword(e.target.value)}
            />
            <button type="submit" className="btn btn-success">
              Search
            </button>
          </form>
        </div>
      </div>
      <div className="table-responsive">
        {complainList && complainList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('ikibazo')}>
                  Ikibazo <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('icyakozwe')}>
                  Icyakozwe <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('icyakorwa')}>
                  Icyakorwa <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('umwanzuro')}>
                  Umwanzuro <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('priority')}>
                  Priority <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  Created At <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  Updated At <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Category <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Umuturage <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {complainList.map((complain, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{complain.ikibazo}</td>
                  <td>{complain.icyakozwe}</td>
                  <td>{complain.icyakorwa}</td>
                  <td>{complain.umwanzuro}</td>
                  <td>{complain.status}</td>
                  <td>{complain.priority}</td>
                  <td>{complain.createdAt ? <TextFormat type="date" value={complain.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{complain.updatedAt ? <TextFormat type="date" value={complain.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{complain.category ? <Link to={`/category/${complain.category.id}`}>{complain.category.name}</Link> : ''}</td>
                  <td>{complain.umuturage ? <Link to={`/umuturage/${complain.umuturage.id}`}>{complain.umuturage.amazina}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/complain/${complain.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/complain/${complain.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/complain/${complain.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Complains found</div>
        )}
      </div>
      {totalItems ? (
        <div className={complainList && complainList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Complain;
