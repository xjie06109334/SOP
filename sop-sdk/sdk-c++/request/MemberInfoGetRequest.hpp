#ifndef SDK_CXX_MEMBERINFOGETREQUEST_HPP
#define SDK_CXX_MEMBERINFOGETREQUEST_HPP

#include <string>
#include "BaseRequest.h"

using namespace std;

class MemberInfoGetRequest : public BaseRequest {

public:
    string getMethod() override;
    string getVersion() override;
    RequestType getRequestType() override;
};

string MemberInfoGetRequest::getMethod() {
    return "member.info.get";
}

string MemberInfoGetRequest::getVersion() {
    return "1.0";
}

RequestType MemberInfoGetRequest::getRequestType() {
    return GET;
}

#endif //SDK_CXX_MEMBERINFOGETREQUEST_HPP
