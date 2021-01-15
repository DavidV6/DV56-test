# Build stage 0
FROM node:13-alpine

WORKDIR /buildroot

COPY . ./

RUN npm install
RUN npm run build

# Build stage 1
FROM node:13-alpine

WORKDIR /app

COPY --from=0 "/buildroot/build" /app
RUN npm install -g serve
CMD ["serve", "-s", "."]
