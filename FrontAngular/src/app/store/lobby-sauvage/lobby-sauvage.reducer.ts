import {createFeature, createFeatureSelector, createReducer, createSelector, on} from '@ngrx/store';
import {createEntityAdapter, EntityAdapter, EntityState} from '@ngrx/entity';
import {LobbySauvage} from './lobby-sauvage.model';
import {LobbySauvageActions} from './lobby-sauvage.actions';

export const lobbySauvagesFeatureKey = 'lobbySauvages';
export const selectLobbySauvageState = createFeatureSelector<LobbySauvageState>(lobbySauvagesFeatureKey);

export interface LobbySauvageState extends EntityState<LobbySauvage> {
  currentLobby: LobbySauvage | undefined
}

export function selectLobbyId(a: LobbySauvage): number {
  return a.id;
}

export const lobbySauvageAdapter: EntityAdapter<LobbySauvage> = createEntityAdapter<LobbySauvage>({
  selectId: selectLobbyId,
});

export const initialState: LobbySauvageState = lobbySauvageAdapter.getInitialState({
  currentLobby: undefined
});

export const reducer = createReducer(
  initialState,
  //TODO failures
  on(LobbySauvageActions.getAllLobbySauvageSuccess,
    (state, action) => {
      return lobbySauvageAdapter.setAll(action.lobbySauvages, state);
    }),
  on(LobbySauvageActions.joinLobby,
    LobbySauvageActions.getCurrentLobbySuccess,
    (state, action) => {
      return {...state, currentLobby: action.lobby};
    }),
  on(LobbySauvageActions.leaveLobbySuccess,
    LobbySauvageActions.getCurrentLobbyFailure,
    (state, action) => {
      return initialState;
    }),
  on(LobbySauvageActions.createLobbySauvageSuccess,
    (state, action) => lobbySauvageAdapter.addOne(action.lobbySauvage, state)),
  on(LobbySauvageActions.addLobbySauvage,
    (state, action) => lobbySauvageAdapter.addOne(action.lobbySauvage, state)
  ),
  on(LobbySauvageActions.upsertLobbySauvage,
    (state, action) => lobbySauvageAdapter.upsertOne(action.lobbySauvage, state)
  ),
  on(LobbySauvageActions.addLobbySauvages,
    (state, action) => lobbySauvageAdapter.addMany(action.lobbySauvages, state)
  ),
  on(LobbySauvageActions.upsertLobbySauvages,
    (state, action) => lobbySauvageAdapter.upsertMany(action.lobbySauvages, state)
  ),
  on(LobbySauvageActions.updateLobbySauvage,
    (state, action) => lobbySauvageAdapter.updateOne(action.lobbySauvage, state)
  ),
  on(LobbySauvageActions.updateLobbySauvages,
    (state, action) => lobbySauvageAdapter.updateMany(action.lobbySauvages, state)
  ),
  on(LobbySauvageActions.deleteLobbySauvage,
    (state, action) => lobbySauvageAdapter.removeOne(action.id, state)
  ),
  on(LobbySauvageActions.deleteLobbySauvages,
    (state, action) => lobbySauvageAdapter.removeMany(action.ids, state)
  ),
  on(LobbySauvageActions.loadLobbySauvages,
    (state, action) => lobbySauvageAdapter.setAll(action.lobbySauvages, state)
  ),
  on(LobbySauvageActions.clearLobbySauvages,
    state => lobbySauvageAdapter.removeAll(state)
  ),
);

export const lobbySauvagesFeature = createFeature({
  name: lobbySauvagesFeatureKey,
  reducer,
  extraSelectors: ({selectLobbySauvagesState}) => ({
    ...lobbySauvageAdapter.getSelectors(selectLobbySauvagesState)
  }),
});

export const selectLobbyById = (id: number) => createSelector(
  selectEntities,
  (entities) => entities[id]
);
export const selectCurrentLobby = () => createSelector(selectLobbySauvageState, state => {
  return state.currentLobby;
});

export const {
  selectIds,
  selectEntities,
  selectAll: selectAllLobbySauvages,
  selectTotal,
} = lobbySauvagesFeature;
