import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Update } from '@ngrx/entity';

import { LobbySauvage } from './lobby-sauvage.model';

export const LobbySauvageActions = createActionGroup({
  source: 'LobbySauvage/API',
  events: {
    'Load LobbySauvages': props<{ lobbySauvages: LobbySauvage[] }>(),
    'Create LobbySauvage': emptyProps(),
    'Create LobbySauvage Success': props<{ lobbySauvage: LobbySauvage }>(),
    'Create LobbySauvage Failure': props<{ error: String }>(),
    'GetAll LobbySauvage Failure': props<{ error: String }>(),
    'GetAll LobbySauvage': emptyProps(),
    'Get Current Lobby': emptyProps(),
    'Get Current Lobby Success': props<{ lobby: LobbySauvage }>(),
    'Get Current Lobby Failure': props<{ error: String }>(),
    'Join Lobby': props<{ lobby: LobbySauvage }>(),
    'Join Lobby success': props<{ lobby: LobbySauvage }>(),
    'Leave Lobby': props<{ lobby: LobbySauvage }>(),
    'Leave Lobby Success': emptyProps(),
    'Update Current Lobby': emptyProps(),
    'Update Current Lobby Success': props<{ lobby: LobbySauvage }>(),
    'Update Current Lobby Failure': props<{ error: String }>(),
    'GetAll LobbySauvage Success': props<{ lobbySauvages: LobbySauvage[] }>(),
    'Add LobbySauvage': props<{ lobbySauvage: LobbySauvage }>(),
    'Upsert LobbySauvage': props<{ lobbySauvage: LobbySauvage }>(),
    'Add LobbySauvages': props<{ lobbySauvages: LobbySauvage[] }>(),
    'Upsert LobbySauvages': props<{ lobbySauvages: LobbySauvage[] }>(),
    'Update LobbySauvage': props<{ lobbySauvage: Update<LobbySauvage> }>(),
    'Update LobbySauvages': props<{ lobbySauvages: Update<LobbySauvage>[] }>(),
    'Delete LobbySauvage': props<{ id: string }>(),
    'Delete LobbySauvages': props<{ ids: string[] }>(),
    'Clear LobbySauvages': emptyProps(),
  }
});
