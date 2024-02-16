arr=(
        "follow"
        "getfeed"
        "getfollowers"
        "getfollowerscount"
        "getfollowing"
        "getfollowingcount"
        "getstory"
        "getuser"
        "isfollower"
        "login"
        "logout"
        "poststatus"
        "register"
        "unfollow"
        "tweeter_sqs_getfollowers"
        "tweeter_sqs_addstatus"
    )
for FUNCTION_NAME in "${arr[@]}"
do
  aws lambda update-function-code --function-name $FUNCTION_NAME --zip-file fileb://C:/Users/pratt/Documents/FALL\ 2022/CS\ 340/TWEETER/tweeter/server/build/libs/server-all.jar &
done