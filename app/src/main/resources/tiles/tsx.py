import xml.etree.ElementTree as ET

# Fungsi untuk membaca dan mendapatkan rentang ID tile dari file .tmx
def get_tmx_tile_ranges(tmx_file):
    tree = ET.parse(tmx_file)
    root = tree.getroot()
    
    # Mendapatkan semua tileset yang digunakan dalam peta
    tile_ranges = []
    for tileset in root.findall("tileset"):
        first_gid = int(tileset.attrib["firstgid"])

        # Mengecek apakah ada atribut tilecount dan memastikan tilecount > 0
        if 'tilecount' in tileset.attrib:
            tile_count = int(tileset.attrib["tilecount"])
            if tile_count > 0:  # Pastikan tile_count lebih besar dari 0
                end_gid = first_gid + tile_count - 1
                tile_ranges.append((first_gid, end_gid))
            else:
                print(f"Warning: tilecount untuk tileset {tileset.attrib['name']} tidak valid ({tile_count}).")
        else:
            # Jika tidak ada tilecount, hitung jumlah tile berdasarkan elemen <image>
            tile_count = len(tileset.findall("image"))
            if tile_count > 0:
                end_gid = first_gid + tile_count - 1
                tile_ranges.append((first_gid, end_gid))
            else:
                print(f"Warning: Tidak ada tile yang ditemukan untuk tileset {tileset.attrib['name']}.")
    
    return tile_ranges


# Fungsi untuk membaca dan mendapatkan tileset yang ada dalam file .tsx
def get_tsx_tile_info(tsx_file):
    tree = ET.parse(tsx_file)
    root = tree.getroot()
    
    # Mendapatkan informasi tileset
    tileset_info = {}
    tileset_name = root.attrib["name"]
    tile_width = int(root.attrib["tilewidth"])
    tile_height = int(root.attrib["tileheight"])
    
    # Menyimpan informasi tileset
    tileset_info["name"] = tileset_name
    tileset_info["tilewidth"] = tile_width
    tileset_info["tileheight"] = tile_height
    
    return tileset_info

# Fungsi untuk memeriksa ID tile dalam rentang yang digunakan pada file .tmx
def check_tmx_ids(tmx_file, tsx_file):
    tile_ranges = get_tmx_tile_ranges(tmx_file)
    tileset_info = get_tsx_tile_info(tsx_file)
    
    print(f"Tileset: {tileset_info['name']}")
    print(f"Tile Width: {tileset_info['tilewidth']}, Tile Height: {tileset_info['tileheight']}")
    
    for first_gid, end_gid in tile_ranges:
        print(f"Tileset IDs: {first_gid} to {end_gid} are used in .tmx")

# Contoh penggunaan
tmx_file = "app/src/main/resources/tiles/coba.tmx"  # Ganti dengan path ke file .tmx Anda
tsx_file = "app/src/main/resources/tiles/Chest.tsx"  # Ganti dengan path ke file .tsx Anda

check_tmx_ids(tmx_file, tsx_file)
