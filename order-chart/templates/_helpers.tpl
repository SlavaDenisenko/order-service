{{/*
Expand the name of the chart.
*/}}
{{- define "ordering.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "ordering.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create a complete URL to connect to the order database
*/}}
{{- define "ordering.fullOrderDBUrl" -}}
{{- printf "jdbc:postgresql://%s:%d/%s" .Values.orderService.database.service.name (int .Values.orderService.database.service.port) .Values.orderService.database.name -}}
{{- end }}

{{/*
Create a complete URL to connect to the billing database
*/}}
{{- define "ordering.fullBillingDBUrl" -}}
{{- printf "jdbc:postgresql://%s:%d/%s" .Values.billingService.database.service.name (int .Values.billingService.database.service.port) .Values.billingService.database.name -}}
{{- end }}

{{/*
Create a complete URL to connect to the notification database
*/}}
{{- define "ordering.fullNotificationDBUrl" -}}
{{- printf "jdbc:postgresql://%s:%d/%s" .Values.notificationService.database.service.name (int .Values.notificationService.database.service.port) .Values.notificationService.database.name -}}
{{- end }}

{{/*
Create a complete image name for the database migration tool
*/}}
{{- define "ordering.migration.image" -}}
{{- printf "%s:%s" .Values.migration.image.repository .Values.migration.image.tag -}}
{{- end }}

{{/*
Create a complete image name for the order service
*/}}
{{- define "ordering.orderService.image" -}}
{{- printf "%s:%s" .Values.orderService.image.repository .Values.orderService.image.tag -}}
{{- end }}

{{/*
Create a complete image name for the billing service
*/}}
{{- define "ordering.billingService.image" -}}
{{- printf "%s:%s" .Values.billingService.image.repository .Values.billingService.image.tag -}}
{{- end }}

{{/*
Create a complete image name for the notification service
*/}}
{{- define "ordering.notificationService.image" -}}
{{- printf "%s:%s" .Values.notificationService.image.repository .Values.notificationService.image.tag -}}
{{- end }}

{{/*
Create a complete image name for the order database
*/}}
{{- define "ordering.dbOrder.image" -}}
{{- printf "%s:%s" .Values.orderService.database.image.repository .Values.orderService.database.image.tag -}}
{{- end }}

{{/*
Create a complete image name for the billing database
*/}}
{{- define "ordering.dbBilling.image" -}}
{{- printf "%s:%s" .Values.billingService.database.image.repository .Values.billingService.database.image.tag -}}
{{- end }}

{{/*
Create a complete image name for the notification database
*/}}
{{- define "ordering.dbNotification.image" -}}
{{- printf "%s:%s" .Values.notificationService.database.image.repository .Values.notificationService.database.image.tag -}}
{{- end }}

{{/*
Create a complete image name for the kafka
*/}}
{{- define "ordering.kafka.image" -}}
{{- printf "%s:%s" .Values.kafka.image.repository .Values.kafka.image.tag -}}
{{- end }}

{{/*
Create a complete image name for the zookeeper
*/}}
{{- define "ordering.zookeeper.image" -}}
{{- printf "%s:%s" .Values.zookeeper.image.repository .Values.zookeeper.image.tag -}}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "ordering.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "ordering.labels" -}}
helm.sh/chart: {{ include "ordering.chart" . }}
{{ include "ordering.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "ordering.selectorLabels" -}}
app.kubernetes.io/name: {{ include "ordering.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "ordering.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "ordering.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}
