/***********************************
getAsgDesiredCapacity Function

Takes in an auto scaling group returns the current desired count.

example usage
  getAsgDesiredCapacity (
    autoscalingGroupName: 'name',
    region: env.AWS_REGION,
    accountId: env.DEV_ACCOUNT_ID,
    role: 'ciinabox-v2',
    default_count: '5'
  )

************************************/

import com.base2.ciinabox.aws.AwsClientBuilder
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsRequest

def call(body) {
    def config = body
    def clientBuilder = new AwsClientBuilder([
        region: config.region,
        awsAccountId: config.accountId,
        role: config.role
    ])  
    def client = clientBuilder.asg()
    def desired_capacity = getDesiredCapacity(client, config)
    print(desired_capacity)
    return desired_capacity
}


@NonCPS
def getDesiredCapacity(client, config) {
    def request = new DescribeAutoScalingGroupsRequest()
            .withAutoScalingGroupNames(config.autoscalingGroupName)

    def asgResult = client.describeAutoScalingGroups(request)
    print(asgResult)
    def asgs = asgResult.getAutoScalingGroups()
    print(asgs)
    if (asgs.isEmpty()) {
        throw new GroovyRuntimeException("Nothing found for asg: ${config.autoscalingGroupName}")
    } else {
        return asgs.first().getDesiredCapacity()
    }
}