import axios from 'axios';
import {createAsyncThunk, isFulfilled, isPending} from '@reduxjs/toolkit';

import {cleanEntity} from 'app/shared/util/entity-utils';
import {createEntitySlice, EntityState, IQueryParams, serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {defaultValue, IComplain} from 'app/shared/model/complain.model';
import {Status} from "app/shared/model/enumerations/status.model";
import {Priority} from "app/shared/model/enumerations/priority.model";

const initialState: EntityState<IComplain> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/complains';

// Actions

export const getEntities = createAsyncThunk('complain/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IComplain[]>(requestUrl);
  // console.log(apiUrl);
  // let complains: IComplain[];
  // complains = [
  //   {
  //     "id": 1,
  //     "ikibazo": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakozwe": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakorwa": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "umwanzuro": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "date": "2022-07-13T07:12:04Z",
  //     "status": Status.ORIENTED,
  //     "priority": Priority.LOW,
  //     "category": null,
  //     "umuturage": null,
  //     "organizations": null
  //   },
  //   {
  //     "id": 2,
  //     "ikibazo": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakozwe": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakorwa": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "umwanzuro": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "date": "2022-07-12T17:15:14Z",
  //     "status": Status.ORIENTED,
  //     "priority": Priority.LOW,
  //     "category": null,
  //     "umuturage": null,
  //     "organizations": null
  //   },
  //   {
  //     "id": 3,
  //     "ikibazo": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakozwe": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakorwa": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "umwanzuro": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "date": "2022-07-13T03:47:51Z",
  //     "status": Status.ORIENTED,
  //     "priority": Priority.LOW,
  //     "category": null,
  //     "umuturage": null,
  //     "organizations": null
  //   },
  //   {
  //     "id": 4,
  //     "ikibazo": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakozwe": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakorwa": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "umwanzuro": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "date": "2022-07-13T06:51:52Z",
  //     "status": Status.ORIENTED,
  //     "priority": Priority.LOW,
  //     "category": null,
  //     "umuturage": null,
  //     "organizations": null
  //   },
  //   {
  //     "id": 5,
  //     "ikibazo": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakozwe": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakorwa": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "umwanzuro": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "date": "2022-07-12T14:24:30Z",
  //     "status": Status.ORIENTED,
  //     "priority": Priority.LOW,
  //     "category": null,
  //     "umuturage": null,
  //     "organizations": null
  //   },
  //   {
  //     "id": 6,
  //     "ikibazo": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakozwe": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakorwa": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "umwanzuro": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "date": "2022-07-13T01:10:02Z",
  //     "status": Status.ORIENTED,
  //     "priority": Priority.LOW,
  //     "category": null,
  //     "umuturage": null,
  //     "organizations": null
  //   },
  //   {
  //     "id": 7,
  //     "ikibazo": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakozwe": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "icyakorwa": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "umwanzuro": "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular / React / Vue Web applications and Spring microservices.",
  //     "date": "2022-07-13T00:21:09Z",
  //     "status": Status.ORIENTED,
  //     "priority": Priority.LOW,
  //     "category": null,
  //     "umuturage": null,
  //     "organizations": null
  //   }
  // ]
  // console.log(complains);
  // return complains;

});

export const getEntity = createAsyncThunk(
  'complain/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IComplain>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'complain/create_entity',
  async (entity: IComplain, thunkAPI) => {
    const result = await axios.post<IComplain>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'complain/update_entity',
  async (entity: IComplain, thunkAPI) => {
    const result = await axios.put<IComplain>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'complain/partial_update_entity',
  async (entity: IComplain, thunkAPI) => {
    const result = await axios.patch<IComplain>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'complain/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IComplain>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const ComplainSlice = createEntitySlice({
  name: 'complain',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = ComplainSlice.actions;

// Reducer
export default ComplainSlice.reducer;
