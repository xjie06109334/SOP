FROM java:8
VOLUME /tmp
VOLUME /sop

# 将所有应用放到一个镜像当中
ADD sop-gateway/target/*.jar sop/sop-gateway/sop-gateway.jar
ADD sop-admin/sop-admin-server/target/*.jar sop/sop-admin/sop-admin.jar
ADD sop-website/sop-website-server/target/*.jar sop/sop-website/sop-website.jar
ADD sop-auth/target/*.jar sop/sop-auth/sop-auth.jar
ADD sop-example/sop-story/target/*.jar sop/sop-story/sop-story.jar

# 拷贝启动脚本
COPY docker-entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

ENTRYPOINT ["docker-entrypoint.sh"]
