// Subscribe RHEL system to RHSM

def register(parameters = [:]){
    rhsm_username = parameters['rhsm_username']
    rhsm_password = parameters['rhsm_password']
    rhsm_url = parameters['rhsm_url']
    rhsm_poolid = parameters['rhsm_poolid']

    rhsmUnregister()

    if (env.ENV_AUTH_TYPE[0..3] == "prod") {
        withCredentials([usernamePassword(credentialsId: 'rhsmProdQaCreds', usernameVariable: 'username', passwordVariable: 'password')]) {
        rhsm_username = ${username}
        rhsm_password = ${password}
        }
        serverUrl = pipelineVars.rhsmProdServerUrl

    if (env.ENV_AUTH_TYPE[0..4] == "stage") {
        withCredentials([usernamePassword(credentialsId: 'rhsmStageQaCreds', usernameVariable: 'username', passwordVariable: 'password')]) {
        rhsm_username = ${username}
        rhsm_password = ${password}
        }
        serverUrl = pipelineVars.rhsmStageServerUrl


    if(rhsm_poolid){

        sh '''
            subscription-manager register --serverurl=${rhsm_url} --username=${rhsm_username} --password=${rhsm_password} --serverurl=${serverUrl}
            subscription-manager attach --pool=${rhsm_poolid}
        '''
    }
    else {
        sh '''
            subscription-manager register --serverurl=${rhsm_url} --username=${rhsm_username} --password=${rhsm_password} --serverurl=${serverUrl} --auto-attach --force
        '''
    }

}

// Unsubscribe RHEL system from RHSM

def unregister(parameters = [:]){
    sh '''
        subscription-manager remove --all
        subscription-manager unregister
        subscription-manager clean
    '''
}
