FROM node:19-alpine
ARG api_base_url
WORKDIR /app
COPY package*.json .
RUN npm i --silent
COPY . .
RUN echo "REACT_APP_BASE_URL=${api_base_url}" > .env
EXPOSE 3000
CMD ["yarn", "start"]