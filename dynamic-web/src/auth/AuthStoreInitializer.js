import { keycloakConfiguration } from "./keycloakConfiguration";
import { refresh } from "./actions";
import { createStore } from 'redux';
import authReducers from './reducers';

export const initialize = () => {
    const keycloak = keycloakConfiguration;
    const authStore = createStore(authReducers);

    keycloak
        .init({ promiseType: 'native', onLoad: 'check-sso' })
        .then(() => {
            authStore.dispatch(refresh(keycloak));
            setInterval(() => {
                if (keycloak.authenticated) {
                    keycloak.updateToken()
                        .then(
                            refreshed => {
                                if (refreshed) {
                                    authStore.dispatch(refresh(keycloak));
                                }
                            }, () => {
                                authStore.dispatch(refresh(keycloak));
                            }
                        );
                }
            }, 5000);
        });


    return authStore;
}