
#include "BaseRequest.h"

#include <utility>


vector<FileInfo> BaseRequest::getFiles() {
    return this->files;
}

void BaseRequest::addFile(const FileInfo& filepath) {
    this->files.push_back(filepath);
}

void BaseRequest::setFiles(vector<FileInfo> files) {
    this->files = std::move(files);
}
