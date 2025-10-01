#!/bin/bash

# Definir os diretórios
DIRETORIO_1="../../storage/images"

# Excluir as pastas, se existirem
rm -rf $DIRETORIO_1

# Criar as pastas novamente
mkdir -p $DIRETORIO_1
mkdir -p $($DIRETORIO_1 + "/users")

echo "Pastas recriadas com sucesso."

# Definir os caminhos das imagens e destinos
backup_folder="../../storage/backup"
source_image2=$backup_folder+"/mock-avatar.jpg"
dest_folder_users=$DIRETORIO_1 +"/users"

# Criar a pasta de backup caso não exista
mkdir -p "$backup_folder"

# Copiar as imagens para a pasta de backup
# cp "$source_image2" "$backup_folder"

# Certificar-se que as pastas de destino existem
mkdir -p "$dest_folder_users"

# Copiar as imagens para as pastas de destino
cp "$source_image2" "$dest_folder_users"

echo "Imagens copiadas e distribuídas com sucesso!"
